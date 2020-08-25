import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DevHistoryMessagesComponent } from './dev-history-messages/dev-history-messages.component';
import { DevHistoryTranslationsComponent } from './dev-history-translations/dev-history-translations.component';
import { DevMessagesComponent } from './dev-messages/dev-messages.component';
import { DevMessagesTableComponent } from './dev-messages/dev-messages-table/dev-messages-table.component';
import { DevProjectsComponent } from './dev-projects/dev-projects.component';
import { DevProjectsTableComponent } from './dev-projects/dev-projects-table/dev-projects-table.component';
import { DevelopersComponent } from './developers.component';
import { DevProjectFormComponent } from './dev-messages/dev-project-form/dev-project-form.component';
import { AggregateInfoComponent } from './aggregate-info/aggregate-info.component';
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


@NgModule({
	declarations: [
		DevHistoryMessagesComponent,
		DevHistoryTranslationsComponent,
		DevMessagesComponent,
		DevMessagesTableComponent,
		DevProjectFormComponent,
		DevProjectsComponent,
		DevProjectsTableComponent,
		DevelopersComponent,
		AggregateInfoComponent
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
		ViewsModule
	],
	providers: [LoginService, UtilsService, SnackbarService, CookieService,
		{ provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: { duration: 5000 } },
		{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }],
	entryComponents: [ConfirmationDialogComponent]
})
export class DevelopersModule {
}
