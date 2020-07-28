import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HomeComponent} from './features/home/home.component';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {APP_CONFIG} from "./app.config";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {SearchComponent} from './features/search/search.component';
import {MatInputModule} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {CategoriesComponent} from './features/categories/categories.component';
import {MatListModule} from "@angular/material/list";
import {MatCardModule} from "@angular/material/card";
import {HotProductComponent} from './features/hot-product/hot-product.component';
import {FeaturedComponent} from './features/featured/featured.component';
import {ProductComponent} from './features/product/product.component';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, APP_CONFIG.apiBaseUrl + 'api/v1/exportCD/message/getByLocale/4/', '');
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SearchComponent,
    CategoriesComponent,
    HotProductComponent,
    FeaturedComponent,
    ProductComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    TranslateModule.forRoot({
      defaultLanguage: 'en_GB',
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatListModule,
    MatCardModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
