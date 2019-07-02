import { NgModule } from '@angular/core';
import { AccessDeniedComponent } from './access-denied/access-denied.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { HomepageComponent } from './homepage/homepage.component';

@NgModule({
	declarations: [
		HomepageComponent,
		PageNotFoundComponent,
		AccessDeniedComponent
	],
	imports: [],
	exports: []
})
export class ViewsModule {
}
