var fs = require("fs");
var fse = require("fs-extra");
var filter = require("gulp-filter");
var multimatch = require("multimatch");
var path = require("path");

var modulesCfg = fse.readJsonSync("./modules1.json");

var aliases = modulesCfg.aliases;

var modulePatterns = getModulePatterns();

console.log(JSON.stringify(modulePatterns));

function getModulePatterns() {
    var modulePatterns = [];
    modulesCfg.modules.forEach(function (module) {
        var modulePath = module.path;
        var moduleMap = module.map;
        for (var mapPath in moduleMap) {
            var moduleMapElement = moduleMap[mapPath];
            var normalizedModuleMapElement = replaceAliases(moduleMapElement);
            var destPath = modulePath + mapPath;
            modulePatterns.push({ "destPath": destPath, "pattern": normalizedModuleMapElement });
        }
    }, this);
    return modulePatterns;
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

var moduleFilter = filter(function (file) {
    var dests = getDestination(file.basename);
});

function getDestinations(fileName) {
    var destinations = [];
    for(var i in modulePatterns) {
        var modulePattern = modulePatterns[i];
        var destPath = modulePatterns[i].destPath;
        var pattern = modulePatterns[i].pattern;
        var matched = multimatch(fileName, pattern, {matchBase: true});
        if (matched.length > 0) {
            destinations.push(destPath);
        }
    }
    return destinations;
}
path.re

console.log(path.relative("c:\\workspaces\\com.ebase.eox\\frontend\\", 
    "c:\\workspaces\\com.ebase.eox\\frontend\\app\\app.component.js"));
console.log(JSON.stringify(getDestinations('app\\app.component.js')));