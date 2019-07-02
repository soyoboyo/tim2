import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NothingFoundComponent } from './components/nothing-found/nothing-found.component';
import { ShortenPipe } from './pipes/shorten/shorten.pipe';
import { OutdatedPipe } from './pipes/outdated/outdated.pipe';
import { OutdatedTranslationPipe } from './pipes/outdated-translation/outdated-translation.pipe';
import { RouterModule } from '@angular/router';
import { routes } from '../routes';
import { HttpClientModule } from '@angular/common/http';
import { ConfirmationDialogComponent } from './components/confirmation-dialog/confirmation-dialog.component';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material';
import { UtilsService } from './services/utils-service/utils.service';
import { SnackbarService } from './services/snackbar-service/snackbar.service';
import { NavbarService } from './services/navbar-service/navbar.service';
import { RestService } from './services/rest/rest.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from '../material.module';


@NgModule({
	declarations: [
		ConfirmationDialogComponent,
		NothingFoundComponent,
		ShortenPipe,
		OutdatedPipe,
		OutdatedTranslationPipe
	],
	imports: [
		RouterModule.forRoot(routes),
		BrowserModule,
		HttpClientModule,
		BrowserAnimationsModule,
		MaterialModule
	],
	exports: [
		ShortenPipe,
		NothingFoundComponent,
		ConfirmationDialogComponent
	],
	providers: [NavbarService, RestService, UtilsService, SnackbarService,
		{ provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: { duration: 5000 } }],
	entryComponents: [ConfirmationDialogComponent]
})
export class SharedModule {
}
