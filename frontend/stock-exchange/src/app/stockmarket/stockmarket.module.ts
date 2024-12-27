// Angular Imports
import {NgModule} from '@angular/core';

// This Module's Components
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {StockmarketComponent} from './stockmarket.component';
import {StockmarketRoutingModule} from "./stockmarket-routing.module";

@NgModule({
  declarations: [StockmarketComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    StockmarketRoutingModule
  ]
})
export class StockmarketModule {

}
