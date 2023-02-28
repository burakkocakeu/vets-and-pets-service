import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.css']
})
export class ConfirmationModalComponent {
  @Output() deleteConfirmed = new EventEmitter();

  onCancel() {
    // emit a "cancel" event to the parent component
    this.deleteConfirmed.emit(false);
  }

  onDelete() {
    // emit a "delete" event to the parent component
    this.deleteConfirmed.emit(true);
  }
}
