// Angular Imports
import { NgModule } from '@angular/core';
import { RegistrationRoutingModule } from './registration-routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';
// This Module's Components
import { RegistrationComponent } from './registration.component';

@NgModule({
    imports: [
        RegistrationRoutingModule,
        FormsModule,
        CommonModule,
        ReactiveFormsModule
    ],
    declarations: [
        RegistrationComponent,
    ]
})
export class RegistrationModule {

}
