import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core'
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from '@angular/material';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [TranslateModule, RouterModule, FlexLayoutModule, MaterialModule, FormsModule],
    exports: [CommonModule, HttpModule, TranslateModule, RouterModule, MaterialModule, FlexLayoutModule, FormsModule]
})
export class SharedModule { }