import {
  selectUser,
  selectUserState,
  selectUserStatus,
} from '../../../store/user/userSelectors';

describe('user selectors', () => {
  const state = {
    user: {
      status: 'received',
      user: {id: 1},
      error: '',
    },
  };

  test('select user state', () => {
    expect(selectUserState(state)).toEqual(state.user);
  });

  test('select user', () => {
    expect(selectUser(state)).toEqual({id: 1});
  });

  test('select user status', () => {
    expect(selectUserStatus(state)).toBe('received');
  });
});
