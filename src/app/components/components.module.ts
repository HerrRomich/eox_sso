import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from '@angular/material';

import { SharedModule } from 'app/shared/shared.module';
import { StartPageComponent } from './start-page.component';
import { ToolbarComponent } from './toolbar.component';
import { LoginComponent } from './login.component';

@NgModule({
    imports: [SharedModule, FlexLayoutModule, MaterialModule],
    exports: [StartPageComponent, ToolbarComponent, LoginComponent],
    declarations: [StartPageComponent, ToolbarComponent, LoginComponent],
    providers: [],
})
export class ComponentsModule { }
