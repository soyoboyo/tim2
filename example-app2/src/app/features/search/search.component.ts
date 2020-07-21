import {Component, OnInit} from '@angular/core';
import {FormControl} from "@angular/forms";
import {Observable} from "rxjs";
import {map, startWith} from "rxjs/operators";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  searchControl = new FormControl();
  options: string[] = ['Bike', 'Car', 'IPhone'];
  filteredOptions: Observable<string[]>;

  languages = [
    {value: 'en_GB', viewValue: 'English'},
    {value: 'pl_PL', viewValue: 'Polish'},
    {value: 'ko_KR', viewValue: 'Korean'}
  ];

  selected = this.languages[0].value;

  constructor(private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.filteredOptions = this.searchControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }

  changeLanguage(event) {
    if (event.isUserInput) {
      this.translate.use(event.source.value);
      console.log(event.source.value)
    }
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().indexOf(filterValue) === 0)
  }

}
