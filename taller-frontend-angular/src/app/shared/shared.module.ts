import { NgModule } from "@angular/core";
import { NgxSpinnerModule } from "ngx-spinner";
import { NgxUpperCaseDirectiveModule } from "ngx-upper-case-directive";
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { VerticalMenuComponent } from './components/vertical-menu/vertical-menu.component';
import { MaterialModule } from './material.module';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { InternalPropertyPipe } from "./pipes/internal-property.pipe";
import { FlexLayoutModule } from "@angular/flex-layout";

@NgModule({
    declarations: [ 
        FooterComponent, 
        HeaderComponent, 
        VerticalMenuComponent,
        InternalPropertyPipe
    ],
    imports: [ 
        CommonModule,
        NgxUpperCaseDirectiveModule,
        NgxSpinnerModule,
        MaterialModule,
        RouterModule,
        FlexLayoutModule
    ],
    exports: [
        // Modulos
        NgxUpperCaseDirectiveModule,
        RouterModule,
        NgxSpinnerModule,
        FlexLayoutModule,
        // Componentes
        FooterComponent, 
        HeaderComponent,
        VerticalMenuComponent,
        // Pipes
        InternalPropertyPipe
    ],
    providers: []
})
export class SharedModule { }