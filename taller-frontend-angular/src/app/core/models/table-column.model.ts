export interface TableColumn<T> {
    label: string;
    property: keyof T | string;
    type: 'text' | 'image' | 'badge' | 'progress' | 'checkbox' | 'button' | 'boolean' | 'date' | 'color' | 'indicador';
    visible?: boolean;
    cssClasses?: string[];
    extraInfo?: any;
    transform?: string;
}
