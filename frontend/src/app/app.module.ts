import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { MaterialModule } from './material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { routes } from './routes';
import { HomepageComponent } from './components/homepage/homepage.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { DevelopersComponent } from './components/developers/developers.component';
import { TranslatorsComponent } from './components/translators/translators.component';
import { DevProjectsComponent } from './components/developers/dev-projects/dev-projects.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { DevMessagesComponent } from './components/developers/dev-messages/dev-messages.component';
import { DevMessagesTableComponent } from './components/developers/dev-messages/dev-messages-table/dev-messages-table.component';
import { SnackbarService } from './services/snackbar/snackbar.service';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material';
import { UtilsService } from './services/utils/utils.service';
import { ConfirmationDialogComponent } from './components/confirmation-dialog/confirmation-dialog.component';
import { ShortenPipe } from './pipes/shorten/shorten.pipe';
import { DevProjectsTableComponent } from './components/developers/dev-projects/dev-projects-table/dev-projects-table.component';
import { LoginComponent } from './components/login/login.component';
import { LoginService } from './services/login/login.service';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TraMessagesComponent } from './components/translators/tra-messages/tra-messages.component';
import { TraMessagesTableComponent } from './components/translators/tra-messages/tra-messages-table/tra-messages-table.component';
import { OutdatedPipe } from './pipes/outdated/outdated.pipe';
import { OutdatedTranslationPipe } from './pipes/outdated-translation/outdated-translation.pipe';
import { NothingFoundComponent } from './components/nothing-found/nothing-found.component';
import { DevHistoryTranslationsComponent } from './components/developers/dev-history-translations/dev-history-translations.component';
import { DevHistoryMessagesComponent } from './components/developers/dev-history-messages/dev-history-messages.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { AccessDeniedComponent } from './components/access-denied/access-denied.component';

@NgModule({
    declarations: [
        AppComponent,
        HomepageComponent,
        NavbarComponent,
        LoginComponent,
        DevelopersComponent,
        TranslatorsComponent,
        TraMessagesComponent,
        TraMessagesTableComponent,
        DevProjectsComponent,
        DevMessagesComponent,
        DevMessagesTableComponent,
        ConfirmationDialogComponent,
        ShortenPipe,
        DevProjectsTableComponent,
        OutdatedPipe,
        OutdatedTranslationPipe,
        NothingFoundComponent,
        DevHistoryTranslationsComponent,
        DevHistoryMessagesComponent,
        PageNotFoundComponent,
        AccessDeniedComponent
    ],
    imports: [
        RouterModule.forRoot(routes),
        BrowserModule,
        MaterialModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        FlexLayoutModule,
    ],
    exports: [
        FormsModule,
        FlexLayoutModule,
        BrowserAnimationsModule
    ],
    providers: [LoginService, UtilsService,  SnackbarService,
        { provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: { duration: 5000 } }],
    bootstrap: [AppComponent],
    entryComponents: [ConfirmationDialogComponent]
})
export class AppModule {
}
