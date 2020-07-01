import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { MaterialModule } from './material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { routes } from './routes';
import { NavbarComponent } from './layout/navbar/navbar.component';
import { DevelopersComponent } from './features/developers/developers.component';
import { TranslatorsComponent } from './features/translators/translators.component';
import { DevProjectsComponent } from './features/developers/dev-projects/dev-projects.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { DevMessagesComponent } from './features/developers/dev-messages/dev-messages.component';
import { DevMessagesTableComponent } from './features/developers/dev-messages/dev-messages-table/dev-messages-table.component';
import { SnackbarService } from './shared/services/snackbar-service/snackbar.service';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material/snack-bar';
import { UtilsService } from './shared/services/utils-service/utils.service';
import { ConfirmationDialogComponent } from './shared/components/confirmation-dialog/confirmation-dialog.component';
import { DevProjectsTableComponent } from './features/developers/dev-projects/dev-projects-table/dev-projects-table.component';
import { LoginComponent } from './core/login-component/login.component';
import { LoginService } from './core/login-service/login.service';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TraMessagesComponent } from './features/translators/tra-messages/tra-messages.component';
import { TraMessagesTableComponent } from './features/translators/tra-messages/tra-messages-table/tra-messages-table.component';
import { DevHistoryTranslationsComponent } from './features/developers/dev-history-translations/dev-history-translations.component';
import { DevHistoryMessagesComponent } from './features/developers/dev-history-messages/dev-history-messages.component';
import { ViewsModule } from './views/views.module';
import { SharedModule } from './shared/shared.module';
import { AggregateInfoComponent } from './features/developers/aggregate-info/aggregate-info.component';
import { CookieService } from 'ngx-cookie-service';
import {AuthInterceptor} from './core/AuthInterceptor';

@NgModule({
	declarations: [
		AppComponent,
		NavbarComponent,
		LoginComponent,
		DevelopersComponent,
		TranslatorsComponent,
		TraMessagesComponent,
		TraMessagesTableComponent,
		DevProjectsComponent,
		DevMessagesComponent,
		DevMessagesTableComponent,
		DevProjectsTableComponent,
		DevHistoryTranslationsComponent,
		DevHistoryMessagesComponent,
		AggregateInfoComponent
	],
	imports: [
		RouterModule.forRoot(routes),
		SharedModule,
		BrowserModule,
		MaterialModule,
		ReactiveFormsModule,
		BrowserAnimationsModule,
		FormsModule,
		ReactiveFormsModule,
		HttpClientModule,
		FlexLayoutModule,
		ViewsModule
	],
	exports: [
		FormsModule,
		FlexLayoutModule,
		BrowserAnimationsModule
	],
	providers: [LoginService, UtilsService, SnackbarService, CookieService,
		{ provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: { duration: 5000 } },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true } ],
	bootstrap: [AppComponent],
	entryComponents: [ConfirmationDialogComponent]
})
export class AppModule {
}
