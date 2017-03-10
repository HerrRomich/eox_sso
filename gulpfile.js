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
var glob = require("glob");
var plumber = require("gulp-plumber");
var through2 = require("through2");
var argv = require("yargs").argv;

const contentDirName = 'WEB-CONTENT';
const webResourceDescFileName = 'OSGI-INF/web-resources.json';

var modulesCfg = fse.readJsonSync("./modules.json");

var aliases = modulesCfg.aliases;

var moduleMaps = getModuleMaps();

function getModuleMaps(moduleName) {
    var moduleMaps = [];
    modulesCfg.modules.forEach(module => {
        if (!moduleName || module.name === moduleName) {
            var modulePath = replaceAlias(module.path);
            module.content.forEach(contentElement => {
                var contentElementMap = contentElement.map;
                for (var mapPath in contentElementMap) {
                    var mapValue = contentElementMap[mapPath];
                    var normalizedMapValue = replaceAliases(mapValue);
                    var destPath = path.resolve(modulePath, contentDirName, replaceAlias(mapPath));
                    moduleMaps.push({ "destPath": destPath, "pattern": normalizedMapValue });
                }
            });
        }
    });
    return moduleMaps;
}

function flattenMaps(moduleMaps) {
    var flattenedPatterns = [];
    moduleMaps.forEach(moduleMap => {
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
        aliasedPaths.forEach(aliasedPath => {
            var normalizedPath = replaceAlias(aliasedPath);
            normalizedPaths.push(normalizedPath);
        });
        return normalizedPaths;
    } else {
        return replaceAlias(aliasedPaths);
    }
}

function replaceAlias(aliasedPath) {
    var matched = aliasedPath.match(/!?(@[^\/]*)/);
    if (!matched)
        return aliasedPath;
    var matchedAlias = matched[1];
    if (!matchedAlias)
        return aliasedPath;
    var substitution = aliases[matchedAlias];
    if (!substitution)
        return aliasedPath;
    return aliasedPath.replace(matchedAlias, substitution);
}

function getFileModuleMaps(fileName) {
    var fileModuleMaps = [];
    moduleMaps.forEach(moduleMap => {
        var pattern = moduleMap.pattern;
        var matched = multimatch(fileName, pattern, { matchBase: true });
        if (matched.length > 0) {
            fileModuleMaps.push(moduleMap);
        }
    });
    return fileModuleMaps;
}

function testFileEtension(file, extension) {
    var ext = path.extname(file.path);
    return ext === extension;
}

function pipeBuildTask(stream) {
    const tsProject = ts.createProject('tsconfig.json');
    const tsFilter = filter(file => testFileEtension(file, '.ts'), { restore: true });
    const scssFilter = filter(file => testFileEtension(file, '.scss'), { restore: true });

    var result = stream;
    result = result.pipe(tsFilter)
        .pipe(sourcemaps.init())
        .pipe(tsProject())
        .pipe(sourcemaps.write())
        .pipe(tsFilter.restore)
        .pipe(scssFilter)
        .pipe(sass())
        .pipe(scssFilter.restore)
        .pipe(foreach( (stream, file) => {
            var relPath = path.relative(file.cwd, file.path);
            var fileModuleMaps = getFileModuleMaps(relPath);
            result = stream;
            fileModuleMaps.forEach(function (fileModuleMap) {
                result = result.pipe(gulp.dest(function (file) {
                    //util.log('Dest file: ' + file.path);
                    return fileModuleMap.destPath;
                }))
            });
            return result;
        }));
    return result;
}

function getBuildTask(pattern) {
    var result = gulp.src(pattern);
    result = pipeBuildTask(result);
    return result;
}

function cleanModule(module) {
    var modulePath = replaceAlias(module.path);
    var webResourceDescriptions = [];
    var webContentPath = path.resolve(modulePath, contentDirName);
    var webResourceDescFilePath = path.resolve(modulePath, webResourceDescFileName);
    try {
        util.log('deleting web resource description: ' + webResourceDescFilePath);
        fse.unlinkSync(webResourceDescFilePath);
        util.log("emptying content: " + webContentPath);
        fse.emptyDirSync(webContentPath);
        fse.removeSync(webContentPath);
    } catch (error) {
        util.log(error);
    }
}

// Deletes whole content of modules
gulp.task('clean', function () {
    var moduleName = argv.module;
    var modules = getModulesByName(moduleName);
    if (!modules.length)
        return;
    util.log("Task 'clean': started...");

    modules.forEach(function (module) {
        if (!moduleName || module.name === moduleName) {
            cleanModule(module);
        }
    })
    util.log("Task 'clean': done.");
});

function createWebResourceDescForModule(module) {
    var modulePath = replaceAlias(module.path);
    var webResourceDescriptions = [];
    module.content.forEach(function (contentElement) {
        var webResourcePath = path.posix.resolve("/", contentDirName, contentElement.path);
        var webResourceDescription = {
            "path": webResourcePath,
            "pattern": contentElement.pattern,
            "contextName": contentElement.contextName
        };
        webResourceDescriptions.push(webResourceDescription);
    });
    var webResourceDescFilePath = path.resolve(modulePath, webResourceDescFileName);
    util.log('creating web resource description: ' + webResourceDescFilePath);
    try {
        fse.mkdirpSync(path.dirname(webResourceDescFilePath));
        fs.writeFileSync(webResourceDescFilePath, JSON.stringify(webResourceDescriptions));
    } catch (error) {
        util.log(error);
    }
}

function getModulesByName(moduleName) {
    return modulesCfg.modules.filter(function (module) {
        return (!moduleName || (module.name === moduleName))
    })
}

gulp.task('web-resources', function () {
    var moduleName = argv.module;
    var modules = getModulesByName(moduleName);
    if (!modules.length)
        return;
    util.log("Task 'web-resources': started...");
    modules.forEach(function (module) {
        if (!moduleName || module.name === moduleName) {
            createWebResourceDescForModule(module);
        }
    })
    util.log("Task 'web-resources': done.");
});

gulp.task('build', ['clean', 'web-resources'], function () {
    var moduleName = argv.module;
    var maps = getModuleMaps(moduleName);
    const flattenedPattern = flattenMaps(maps);
    if (!flattenedPattern.length)
        return;

    return getBuildTask(flattenedPattern);
})

const addChangeFilter = filter(file => {
    var result = (file.event === 'add' || file.event === 'change');
    return result;
}, { restore: true, passthrough: true });

const unlinkFilter = filter(file => {
    var result = (file.event === 'unlink');
    return result;
}, { restore: true });

Array.prototype.unique = function () {
    var seen = {};
    var out = [];
    var len = this.length;
    var j = 0;
    for (var i = 0; i < len; i++) {
        var item = this[i];
        if (seen[item] !== 1) {
            seen[item] = 1;
            out[j++] = item;
        }
    }
    return out;
};

function batchUnlink() {
    var moduleMapsToUnlink = [];
    var holdOn;
    var timeout;
    var event;

    function setupFlushTimeout() {
        if (!holdOn && moduleMapsToUnlink.length) {
            util.log('Timer started: ' + JSON.stringify(moduleMapsToUnlink));
            timeout = setTimeout(flush, 100);
        }
    }

    function flush() {
        holdOn = true;
        var currentModuleMapsToUnlink = moduleMapsToUnlink;
        moduleMapsToUnlink = [];
        currentModuleMapsToUnlink.forEach(function (fileModuleMap) {
            util.log('deleting ' + fileModuleMap.destPath);
            fse.emptyDirSync(fileModuleMap.destPath);
        });
        const flattenedFilePattern = flattenMaps(currentModuleMapsToUnlink);
        util.log('rebuild ' + JSON.stringify(flattenedFilePattern));

        var task = gulp.add('rebuild_task', function () { return getBuildTask(flattenedFilePattern) });
        if (!event) {
            event = task.on('task_stop', function (e) {
                util.log('task done');
                holdOn = false;
                setupFlushTimeout();
            });
        }
        gulp.start('rebuild_task');
    }

    function addModuleToUnlink(file, enc, cb) {
        util.log('Unlink file: ' + file);
        if (file.event !== 'unlink')
            return;
        var relPath = path.relative(file.cwd, file.path);
        util.log('Unlink file: ' + relPath);
        var fileModuleMaps = getFileModuleMaps(relPath);
        util.log('found mudule maps: ' + JSON.stringify(fileModuleMaps));
        moduleMapsToUnlink = moduleMapsToUnlink.concat(fileModuleMaps).unique();
        util.log('moduleMapsToUnlink: ' + JSON.stringify(moduleMapsToUnlink));
        if (timeout) { clearTimeout(timeout); }

        setupFlushTimeout();

        cb(null, file);
    }

    var stream = through2.obj(addModuleToUnlink);
    return stream;
}

gulp.task('development', ['build'], function () {
    const flattenedPatterns = flattenMaps(moduleMaps);
    var stream = watch(flattenedPatterns, { ignoreInitial: true })
        .pipe(unlinkFilter)
        .pipe(batchUnlink())
        .pipe(unlinkFilter.restore);
    stream = pipeBuildTask(stream);
    return stream;
});