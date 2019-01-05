import { FormBuilder, FormGroup, FormArray, FormControl, Validators} from '@angular/forms';
import { Component, OnInit, Input } from '@angular/core';


@Component({
  selector: 'app-new-campaign-form',
  templateUrl: './new-campaign-form.component.html',
  styleUrls: ['./new-campaign-form.component.css']
})

export class NewCampaignFormComponent implements OnInit {
  @Input() showForm: boolean;
  allowed_orientations = ['E', 'S', 'W', 'N'];

  CampaignForm = this.fb.group({
    campName: ['', Validators.required],
    topX: ['', Validators.required],
    topY: ['', Validators.required],
    mowers: this.fb.array([])
  });

  constructor(private fb: FormBuilder){
  }

  ngOnInit() {
  }

  get mowers() {
    return this.CampaignForm.get('mowers') as FormArray;
  }

  createMowForm() : FormGroup {
    return this.fb.group({
      numb: this.mowers.length + 1,
      posX: ['', Validators.required],
      posY: ['', Validators.required],
      posO: ['', Validators.required],
      action_list: ['', Validators.required]
    })
  }

  addMower(){
    let n = this.mowers.length
    this.mowers.push(
      this.createMowForm()
    )
  }

  removeMower(){
    console.log("removing"+this.mowers.length)
    this.mowers.removeAt(this.mowers.length)
  }

  canceling(){
    this.showForm=!this.showForm
  }
}
