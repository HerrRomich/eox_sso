import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ComponentsModule } from 'app/components/components.module';
import { DivaRoutingModule } from './diva-routing.module';
import { DivaStartPageComponent } from './diva-start-page.component'
import { SharedModule } from 'app/shared/shared.module'

@NgModule({
    imports: [DivaRoutingModule, ComponentsModule, SharedModule],
    declarations: [DivaStartPageComponent]
})
export class DivaModule { }
