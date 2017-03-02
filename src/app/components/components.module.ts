import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from '@angular/material'

import { StartPageComponent } from './start-page.component'
import { BrickComponent } from './brick.componen'

@NgModule({
    imports: [FlexLayoutModule, MaterialModule],
    exports: [StartPageComponent, BrickComponent],
    declarations: [StartPageComponent, BrickComponent],
    providers: [],
})
export class ComponentsModule { }
