import { signal } from '@angular/core';

export const HR = [
  {
    icon: 'group',
    label: 'Users',
    route: 'user',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [
      {
        icon: 'list',
        label: 'User List',
        route: 'list',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
      {
        icon: 'add',
        label: 'Add User',
        route: 'create',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
    ],
  },
];
