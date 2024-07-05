import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ToysComponent } from './toys/toys.component';
import { ToyDetailComponent } from './toy-detail/toy-detail.component';
import { BasketComponent } from './basket/basket.component';
import { LoginComponent } from './login/login.component';
import { UserComponent } from './user/user.component';
import { PartnershipComponent } from './partnership/partnership.component';

const routes: Routes = [
  {path: 'toys', component: ToysComponent},
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'detail/:id/:userId', component: ToyDetailComponent},
  {path: 'baskets/:id', component: BasketComponent}, 
  {path: 'login', component: LoginComponent},
  { path: 'user', component: UserComponent },
  { path: 'partnership-applications', component: PartnershipComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
