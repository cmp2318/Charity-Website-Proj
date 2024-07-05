import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';

import { AppComponent } from './app.component';
import { ToysComponent } from './toys/toys.component';
import { AppRoutingModule } from './app-routing.module';
import { ToyDetailComponent } from './toy-detail/toy-detail.component';
import { BasketComponent } from './basket/basket.component';
import { QuantityDropdownComponent } from './quantity-dropdown/quantity-dropdown.component';
import { ToySearchComponent } from './toy-search/toy-search.component';
import { LoginComponent } from './login/login.component';
import { UserComponent } from './user/user.component';
import { PartnershipComponent } from './partnership/partnership.component';


@NgModule({
  declarations: [
    AppComponent,
    ToysComponent,
    ToyDetailComponent,
    BasketComponent,
    QuantityDropdownComponent,
    ToySearchComponent,
    LoginComponent,
    UserComponent,
    PartnershipComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    CommonModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
