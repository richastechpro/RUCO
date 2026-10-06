import { signal } from '@angular/core';

export const SUPERADMIN = [
  {
    icon: 'grid_view',
    label: 'Dashboard',
    route: 'dashboard',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [],
  },

  {
    icon: 'apartment',
    label: 'Companies',
    route: 'company',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [
      {
        icon: 'list',
        label: 'Company List',
        route: 'list',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
    ],
  },

  {
    icon: 'alt_route',
    label: 'Branches',
    route: 'branch',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [
      {
        icon: 'list',
        label: 'Branch List',
        route: 'list',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
      {
        icon: 'add',
        label: 'Add Branch',
        route: 'create',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
    ],
  },

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

  {
    icon: 'account_circle',
    label: 'Customer',
    route: 'customer',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [
      {
        icon: 'list',
        label: 'Customer List',
        route: 'list',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
    ],
  },

  {
    icon: 'local_shipping',
    label: 'Transporters',
    route: 'transporter',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [
      {
        icon: 'list',
        label: 'Transporter List',
        route: 'list',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
      {
        icon: 'add',
        label: 'Add Transporter',
        route: 'create',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
    ],
  },

  {
    icon: 'location_on',
    label: 'States, Cities, Areas',
    route: 'location',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [
      {
        icon: 'list',
        label: 'State List',
        route: 'state/list',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
      {
        icon: 'list',
        label: 'City List',
        route: 'city/list',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
      {
        icon: 'list',
        label: 'Area List',
        route: 'area/list',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
    ],
  },

  {
    icon: 'water_drop',
    label: 'FBOs',
    route: 'fbo',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [
      {
        icon: 'list',
        label: 'FBO List',
        route: 'list',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
      {
        icon: 'add',
        label: 'Add FBO',
        route: 'create',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
    ],
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

  {
    icon: 'inventory',
    label: 'Inventory',
    route: 'inventory',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [
      {
        icon: 'list',
        label: 'Inventory Inward',
        route: 'in',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
      {
        icon: 'list',
        label: 'Inventory Outward',
        route: 'out',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
      {
        icon: 'list',
        label: 'Inventory Available',
        route: 'all',
        selected: signal(false),
        showSubItemsItem: signal(false),
        subItemsItems: [],
      },
    ],
  },

  {
    icon: 'article',
    label: 'Reports',
    route: 'report',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [],
  },

  {
    icon: 'checklist',
    label: 'Approve',
    route: 'approval/list',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [],
  },

  {
    icon: 'contact_phone',
    label: 'Ticket',
    route: 'ticket/list',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [],
  },

   {
    icon: 'currency_exchange',
    label: 'Rates',
    route: 'rates',
    selected: signal(false),
    showSubItem: signal(false),
    subItems: [],
  },
];
