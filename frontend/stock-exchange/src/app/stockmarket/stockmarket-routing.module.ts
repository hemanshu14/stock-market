import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {StockmarketComponent} from "./stockmarket.component";

const routes: Routes = [
  {path: '', component: StockmarketComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StockmarketRoutingModule { }
