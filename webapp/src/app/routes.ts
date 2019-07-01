import { Routes } from '@angular/router';
import { HomepageComponent } from './components/homepage/homepage.component';
import { LoginComponent } from './components/login/login.component';
import { DevelopersComponent } from './components/developers/developers.component';
import { TranslatorsComponent } from './components/translators/translators.component';
import { DevHistoryMessagesComponent } from './components/developers/dev-history-messages/dev-history-messages.component';
import { DevHistoryTranslationsComponent } from './components/developers/dev-history-translations/dev-history-translations.component';
import { AccessDeniedComponent } from './components/access-denied/access-denied.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { AuthGuard } from './auth/auth.guard';

export const routes: Routes = [
	{ path: '', redirectTo: 'login', pathMatch: 'full' },
	{ path: 'homepage', component: HomepageComponent },
	{ path: 'login', component: LoginComponent },
	{ path: 'developer', component: DevelopersComponent, canActivate: [AuthGuard] },
	{ path: 'developer/message/:id', component: DevHistoryMessagesComponent, data: { is: 'id' }, canActivate: [AuthGuard] },
	{ path: 'developer/translation/:id', component: DevHistoryTranslationsComponent, data: { is: 'id' }, canActivate: [AuthGuard] },
	{ path: 'translator', component: TranslatorsComponent, canActivate: [AuthGuard] },
	{ path: 'forbidden', component: AccessDeniedComponent },
	{ path: '**', component: PageNotFoundComponent }
];
