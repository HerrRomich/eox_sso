SystemJS
    .config({
paths: {
            'npm:': '/node_modules/',
            'app:': '/app/'
        },
        map: {
            app: '/app',
            '@angular/core': 'npm:@angular/core/core.umd.js',
            '@angular/common': 'npm:@angular/common/common.umd.js',
            '@angular/compiler': 'npm:@angular/compiler/compiler.umd.js',
            '@angular/forms': 'npm:@angular/forms/forms.umd.js',
            '@angular/material': 'npm:@angular/material/material.umd.js',
            '@angular/http': 'npm:@angular/http/http.umd.js',
            '@angular/router': 'npm:@angular/router/router.umd.js',
            '@angular/flex-layout' : 'npm:@angular/flex-layout/flex-layout.umd.js',
            '@angular/platform-browser': 'npm:@angular/platform-browser/platform-browser.umd.js',
            '@angular/platform-browser-dynamic': 'npm:@angular/platform-browser-dynamic/platform-browser-dynamic.umd.js',
            'rxjs': 'npm:rxjs',
            '@ngx-translate/core': 'npm:@ngx-translate/core/core.umd.js',
            '@ngx-translate/http-loader': 'npm:@ngx-translate/http-loader/http-loader.umd.js',
            'hammerjs': 'npm:hammerjs/hammer.js',
            'angular2-cookie': 'npm:angular2-cookie'
        },
        defaultExtension: 'ts',
        packages: {
            'app': {
                main: 'main.js',
                defaultExtension: 'js'
            },
            rxjs: {
                defaultExtension: 'js'
            },
            'angular2-cookie': {
                main: 'core.js'
            }
        },
    });