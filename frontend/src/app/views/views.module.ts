import { NgModule } from '@angular/core';
import { AccessDeniedComponent } from './access-denied/access-denied.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { HomepageComponent } from './homepage/homepage.component';
import { MaterialModule } from '../material.module';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
	declarations: [
		HomepageComponent,
		PageNotFoundComponent,
		AccessDeniedComponent
	],
	imports: [MaterialModule, TranslateModule],
	exports: []
})
export class ViewsModule {
}
