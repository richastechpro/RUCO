import { Role } from './Roles';
import { SUPERADMIN } from './menus/superadmin';
import { PICKUPBOY } from './menus/pickupboy';
import { FBO_SUPERADMIN } from './menus/fbo_superadmin';
import { FBO_STATE } from './menus/fbo_state';
import { FBO_CITY } from './menus/fbo_city';
import { FBO_BRANCH } from './menus/fbo_branch';
import { MANAGER } from './menus/manager';
import { BRANCH_EXECUTIVE } from './menus/branch-executive';
import { HR } from './menus/hr';

export const getSidemenu = (role: string) => {
  switch (role) {
    case Role.SUPERADMIN:
      return SUPERADMIN;
    case Role.MANAGER:
      return MANAGER;
    case Role.BRANCH_EXECUTIVE:
      return BRANCH_EXECUTIVE;
    case Role.HR:
      return HR;
    case Role.PICKUPBOY:
      return PICKUPBOY;

    case Role.FBO_SUPERADMIN:
      return FBO_SUPERADMIN;
    case Role.FBO_STATE:
      return FBO_STATE;
    case Role.FBO_CITY:
      return FBO_CITY;
    case Role.FBO_BRANCH:
      return FBO_BRANCH;

    default:
      return [];
  }
};
