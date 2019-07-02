import { Routes } from '@angular/router';
import { HomepageComponent } from './views/homepage/homepage.component';
import { LoginComponent } from './core/login-component/login.component';
import { DevelopersComponent } from './features/developers/developers.component';
import { TranslatorsComponent } from './features/translators/translators.component';
import { DevHistoryMessagesComponent } from './features/developers/dev-history-messages/dev-history-messages.component';
import { DevHistoryTranslationsComponent } from './features/developers/dev-history-translations/dev-history-translations.component';
import { AccessDeniedComponent } from './views/access-denied/access-denied.component';
import { PageNotFoundComponent } from './views/page-not-found/page-not-found.component';
import { AuthGuard } from './core/auth-guards/auth.guard';

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
