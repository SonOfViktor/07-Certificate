import axios from 'axios';
import {
  cleanUserStatus,
  createUser,
  loadUser,
  logout,
  userReducer,
} from '../../../store/user/userSlice';

const TOKEN = 'header.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNjc1MTEyNDAwfQ.signature'
jest.mock('axios');

describe('user async actions', () => {
  const dispatch = jest.fn();

  test('load user action', async () => {
    axios.post.mockReturnValue({id: 1, name: 'Uliana'});

    const thunk = loadUser();

    await thunk(dispatch, null, {client: axios, api: {}});
    const [pending, fulfilled] = dispatch.mock.calls;

    expect(pending[0].type).toBe(loadUser.pending().type);
    expect(fulfilled[0].type).toBe(loadUser.fulfilled().type);
    expect(fulfilled[0].payload).toEqual({id: 1, name: 'Uliana'});
  });

  test('load user action fail', async () => {
    axios.post.mockRejectedValue(new Error('This is error'));

    const thunk = loadUser();
    await thunk(dispatch, null, {client: axios, api: {}});
    const actual = dispatch.mock.calls[1][0].payload;

    expect(actual).toBe('This is error');
  });

  test('create user action', async () => {
    axios.post.mockReturnValue({id: 1, name: 'Uliana'});

    const thunk = createUser();

    await thunk(dispatch, null, {client: axios, api: {}});
    const [pending, fulfilled] = dispatch.mock.calls;

    expect(pending[0].type).toBe(createUser.pending().type);
    expect(fulfilled[0].type).toBe(createUser.fulfilled().type);
    expect(fulfilled[0].payload).toEqual({id: 1, name: 'Uliana'});
  });

  test('create user action fail', async () => {
    axios.post.mockRejectedValue(new Error('This is error'));

    const thunk = createUser();
    await thunk(dispatch, null, {client: axios, api: {}});
    const actual = dispatch.mock.calls[1][0].payload;

    expect(actual).toBe('This is error');
  });
});

describe('user reducer', () => {
  const initialState = {
    status: 'received',
    error: '',
    user: {id: 1, name: 'Uliana'},
  };

  test('logout action', () => {
    localStorage.setItem('user', JSON.stringify({id: 1, name: 'Uliana'}));

    const actual = userReducer(initialState, logout());
    const expected = {
      status: 'idle',
      error: '',
      user: null,
    };

    expect(actual).toEqual(expected);
    expect(localStorage.getItem('user')).toBe(null);
  });

  test('clean user status', () => {
    const actual = userReducer(initialState, cleanUserStatus());
    const expected = {...initialState, status: 'idle'};

    expect(actual).toEqual(expected);
  });
});

describe('user extra reducer', () => {
  const initialState = {
    status: 'idle',
    error: '',
    user: null,
  };

  test('load user pending', () => {
    const actual = userReducer(initialState, loadUser.pending());
    const expected = {...initialState, status: 'loading'};

    expect(actual).toEqual(expected);
  });

  test('load user rejected', () => {
    const rejectedAction = {
      type: loadUser.rejected.type,
      payload: 'this is error',
    };

    const actual = userReducer(initialState, rejectedAction);
    const expected = {
      status: 'rejected',
      error: 'this is error',
      user: null,
    };

    expect(actual).toEqual(expected);
  });

  test('load user fulfilled', () => {
    const payload = {
      data: {
        id: 1,
        name: 'Uliana',
        role: 'ROLE_USER',
        token: TOKEN,
      },
    };

    const user = {id: 1, name: 'Uliana', role: 'user', expireTime: 1675114200};
    const actual = userReducer(initialState, loadUser.fulfilled(payload));
    const expected = {
      status: 'received',
      error: '',
      user: user,
    };

    expect(actual).toEqual(expected);
    expect(localStorage.getItem('user')).toEqual(JSON.stringify(user));
    expect(localStorage.getItem('token')).toBe(TOKEN);
  });

  afterAll(() => {
    localStorage.clear();
  });

  test('create user pending', () => {
    const actual = userReducer(initialState, createUser.pending());
    const expected = {...initialState, status: 'loading'};

    expect(actual).toEqual(expected);
  });

  test('create user rejected', () => {
    const rejectedAction = {
      type: createUser.rejected.type,
      payload: 'this is error',
    };

    const actual = userReducer(initialState, rejectedAction);
    const expected = {
      status: 'rejected',
      error: 'this is error',
      user: null,
    };

    expect(actual).toEqual(expected);
  });

  test('create user fulfilled', () => {
    const actual = userReducer(initialState, createUser.fulfilled());
    const expected = {...initialState, status: 'created'};

    expect(actual).toEqual(expected);
  });
});
