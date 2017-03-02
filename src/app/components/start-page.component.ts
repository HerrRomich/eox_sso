import { Component, OnInit, Input } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
    moduleId: module.id,
    selector: 'eox-start-page',
    templateUrl: './start-page.component.html',
    styleUrls: ['./start-page.component.css']
})
export class StartPageComponent implements OnInit {

    @Input() footerText: string;

    constructor() {
    }

    ngOnInit() {
    }
}
