import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { MaterialModule } from './material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { routes } from './routes';
import { NavbarComponent } from './layout/navbar/navbar.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { SnackbarService } from './shared/services/snackbar-service/snackbar.service';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material/snack-bar';
import { UtilsService } from './shared/services/utils-service/utils.service';
import { ConfirmationDialogComponent } from './shared/components/confirmation-dialog/confirmation-dialog.component';
import { LoginComponent } from './core/login-component/login.component';
import { LoginService } from './core/login-service/login.service';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ViewsModule } from './views/views.module';
import { SharedModule } from './shared/shared.module';
import { CookieService } from 'ngx-cookie-service';
import { AuthInterceptor } from './core/AuthInterceptor';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { DevelopersModule } from './features/developers/developers.module';
import { TranslatorsModule } from './features/translators/translators.module';
import { HttpLoaderFactory } from '../../../example-app2/src/app/app.module';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { MatPaginatorIntlService } from './core/mat-paginator-intl-service/mat-paginator-intl.service';

@NgModule({
	declarations: [
		AppComponent,
		LoginComponent,
		NavbarComponent
	],
	imports: [
		BrowserAnimationsModule,
		BrowserModule,
		TranslateModule.forRoot({
			defaultLanguage: 'en_GB',
			loader: {
				provide: TranslateLoader,
				useFactory: HttpLoaderFactory,
				deps: [HttpClient]
			}
		}),
		FlexLayoutModule,
		FormsModule,
		HttpClientModule,
		MaterialModule,
		ReactiveFormsModule,
		ReactiveFormsModule,
		RouterModule.forRoot(routes),
		SharedModule,
		ViewsModule,
		DevelopersModule,
		TranslatorsModule
	],
	exports: [
		FormsModule,
		FlexLayoutModule,
		BrowserAnimationsModule
	],
	providers: [LoginService, UtilsService, SnackbarService, CookieService,
		{ provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: { duration: 5000 } },
		{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
		{ provide: MatPaginatorIntl, useClass: MatPaginatorIntlService }],
	bootstrap: [AppComponent],
	entryComponents: [ConfirmationDialogComponent]
})

export class AppModule {
}
