import {Component, forwardRef, Input} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR, ReactiveFormsModule} from "@angular/forms";

type InputTypes = "text" | "email" | "password"

@Component({
  selector: 'app-primary-input',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => PrimaryInputComponent),
      multi: true
    }
  ],
  templateUrl: './primary-input.component.html',
  styleUrl: './primary-input.component.scss'
})
export class PrimaryInputComponent implements ControlValueAccessor{
  @Input() type: InputTypes = "text";
  @Input() placeholder: string = "";
  @Input() label: string = "";
  @Input() inputName: string = "";


  value: string = "";
  OnChange = (value: string) => {}
  OnTouched = () => {}

  onInput(event: Event) {
    const value = (event.target as HTMLInputElement).value
    this.OnChange(value)
  }

  writeValue(value: any) {
    this.value = value;
  }

  registerOnChange(fn: any) {
    this.OnChange = fn;
  }

  registerOnTouched(fn: any) {
    this.OnTouched = fn;
  }

  setDisabledState(isDisabled: boolean) {
  }
}
