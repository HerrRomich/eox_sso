import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { DivaStartPageComponent } from './diva-start-page.component'

const divaRoutes: Routes = [{
    path: '',
    component: DivaStartPageComponent
}];

@NgModule({
    imports: [RouterModule.forChild(divaRoutes)]
})
export class DivaRoutingModule {

}