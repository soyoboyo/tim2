import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TraMessagesComponent } from './tra-messages/tra-messages.component';
import { TraMessagesTableComponent } from './tra-messages/tra-messages-table/tra-messages-table.component';
import { TranslatorFormComponent } from './tra-messages/translator-form/translator-form.component';
import { TranslatorsComponent } from './translators.component';
import { MaterialModule } from '../../material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { routes } from '../../routes';
import { SharedModule } from '../../shared/shared.module';
import { ViewsModule } from '../../views/views.module';
import { LoginService } from '../../core/login-service/login.service';
import { UtilsService } from '../../shared/services/utils-service/utils.service';
import { SnackbarService } from '../../shared/services/snackbar-service/snackbar.service';
import { CookieService } from 'ngx-cookie-service';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material/snack-bar';
import { AuthInterceptor } from '../../core/AuthInterceptor';
import { ConfirmationDialogComponent } from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { TranslateModule } from '@ngx-translate/core';


@NgModule({
	declarations: [
		TraMessagesComponent,
		TraMessagesTableComponent,
		TranslatorFormComponent,
		TranslatorsComponent
	],
	imports: [
		CommonModule,
		BrowserAnimationsModule,
		BrowserModule,
		FlexLayoutModule,
		FormsModule,
		HttpClientModule,
		MaterialModule,
		ReactiveFormsModule,
		ReactiveFormsModule,
		RouterModule.forRoot(routes),
		SharedModule,
		ViewsModule,
		TranslateModule
	],
	providers: [LoginService, UtilsService, SnackbarService, CookieService,
		{ provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: { duration: 5000 } },
		{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }],
	entryComponents: [ConfirmationDialogComponent]
})
export class TranslatorsModule {
}
