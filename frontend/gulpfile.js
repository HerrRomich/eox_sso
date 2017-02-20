var gulp = require('gulp');
var sass = require('gulp-sass');
var ts = require('gulp-typescript');
var util = require('gulp-util');
var fs = require("fs");
var fse = require("fs-extra");
var filter = require("gulp-filter");
var multimatch = require("multimatch");
var path = require("path");
var foreach = require("gulp-foreach");
var watch = require("gulp-watch");
var sourcemaps = require("gulp-sourcemaps");

var modulesCfg = fse.readJsonSync("./modules.json");

var aliases = modulesCfg.aliases;

var moduleMaps = getModuleMaps();

function getModuleMaps() {
    var moduleMaps = [];
    modulesCfg.modules.forEach(function (module) {
        var modulePath = replaceAlias(module.path);
        var moduleMap = module.map;
        for (var mapPath in moduleMap) {
            var moduleMapElement = moduleMap[mapPath];
            var normalizedModuleMapElement = replaceAliases(moduleMapElement);
            var destPath = modulePath + replaceAlias(mapPath);
            moduleMaps.push({ "destPath": destPath, "pattern": normalizedModuleMapElement });
        }
    });
    return moduleMaps;
}

function flattenMaps(moduleMaps) {
    var flattenedPatterns = [];
    moduleMaps.forEach(function (moduleMap) {
        var pattern = moduleMap.pattern;
        if (Array.isArray(pattern)) {
            flattenedPatterns = flattenedPatterns.concat(pattern);
        } else {
            flattenedPatterns.push(pattern);
        }
    });
    return flattenedPatterns;
}

function replaceAliases(aliasedPaths) {
    if (Array.isArray(aliasedPaths)) {
        var normalizedPaths = [];
        for (var i in aliasedPaths) {
            var aliasedPath = aliasedPaths[i];
            var normalizedPath = replaceAlias(aliasedPath);
            normalizedPaths.push(normalizedPath);
        }
        return normalizedPaths;
    } else {
        return replaceAlias(aliasedPaths);
    }
}

function replaceAlias(aliasedPath) {
    var matched = aliasedPath.match(/@[^/]*/);
    if (!matched)
        return aliasedPath;
    var matchedAlias = matched[0];
    if (!matchedAlias)
        return aliasedPath;
    var substitution = aliases[matchedAlias];
    if (!substitution)
        return aliasedPath;
    return aliasedPath.replace(matchedAlias, substitution);
}

function getFileModuleMaps(fileName) {
    var fileModuleMaps = [];
    moduleMaps.forEach(function (moduleMap) {
        var pattern = moduleMap.pattern;
        var matched = multimatch(fileName, pattern, { matchBase: true });
        if (matched.length > 0) {
            fileModuleMaps.push(moduleMap);
        }
    });
    return fileModuleMaps;
}

const tsProject = ts.createProject('tsconfig.json');

function pipeBuildTask(stream, file) {
    var relPath = path.relative(file.cwd, file.path);
    var fileModuleMaps = getFileModuleMaps(relPath);
    var result = stream;
    var ext = path.extname(file.path);
    var baseName = path.basename(file.path);
    console.log('file: ' + baseName);
    if (ext === '.ts') {
        result = result
        .pipe(sourcemaps.init())
        .pipe(tsProject())
        .pipe(sourcemaps.write());
    }
    if (ext === '.scss') {
        result = result.pipe(sass());
    }
    fileModuleMaps.forEach(function (fileModuleMap) {
        result = result.pipe(gulp.dest(fileModuleMap.destPath));
    });
    return result;
}

function getBuildTask(pattern) {
    var result = gulp.src(pattern);
    result = result.pipe(foreach(pipeBuildTask));
    return result;
}


gulp.task('clear', function () {
    util.log("Task 'clear': started...");
    var modules = modulesCfg.modules;
    modules.forEach(function (module) {
        util.log("deleting " + module.path);
        fse.emptyDirSync(module.path);
    });
    util.log("Task 'clear': done.");
});

gulp.task('build', function () {
    const flattenedPattern = flattenMaps(moduleMaps);
    return getBuildTask(flattenedPattern);
})

gulp.task('development', ['clear'], function () {
    const flattenedPatterns = flattenMaps(moduleMaps);
    return watch(flattenedPatterns, { ignoreInitial: false, readDelay: 0 })
        .pipe(foreach(function (stream, file) {
            if (file.event === 'unlink') {
                var relPath = path.relative(file.cwd, file.path);
                util.log("unlinked " + relPath);
                var fileModuleMaps = getFileModuleMaps(relPath);
                fileModuleMaps.forEach(function (fileModuleMap) {
                    util.log("deleting " + fileModuleMap.destPath);
                    fse.emptyDirSync(fileModuleMap.destPath);
                });
                const flattenedFilePattern = flattenMaps(fileModuleMaps);
                util.log("rebuild " + JSON.stringify(flattenedFilePattern));
                getBuildTask(flattenedFilePattern);
                return stream;
            }
            return pipeBuildTask(stream, file);
        }));
});