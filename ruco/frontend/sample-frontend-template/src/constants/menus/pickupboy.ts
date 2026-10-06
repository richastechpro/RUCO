import { signal } from '@angular/core';

export const PICKUPBOY = [
  {
    icon: 'grid_view',
    label: 'Dashboard',
    route: 'dashboard',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [],
  },

  {
    icon: 'apps',
    label: 'Collections',
    route: 'collection',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [
      {
        icon: 'list',
        label: 'Collection List',
        route: 'list',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
    ],
  },
];
