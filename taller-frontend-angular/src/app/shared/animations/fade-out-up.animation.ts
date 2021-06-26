import { animate, style, transition, trigger } from '@angular/animations';

export function fadeOutUpAnimation(duration: number) {
  return trigger('fadeOutUp', [
    transition(':leave', [
      style({
        transform: 'translateY(0)',
        opacity: 1
      }),
      animate(`${duration}ms cubic-bezier(0.35, 0, 0.25, 1)`, style({
        transform: 'translateY(20)',
        opacity: 0
      }))
    ])
  ]);
}

export const fadeOutUp2000ms = fadeOutUpAnimation(2000);
