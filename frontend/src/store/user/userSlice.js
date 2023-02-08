import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';

const TOKEN_TERM = 1800;

export const loadUser = createAsyncThunk(
  '@@user/load-user',
  async (data, {extra: {client, api}, rejectWithValue}) => {
    try {
      return await client.post(api.LOGIN, data);
    } catch (e) {
      return rejectWithValue(e?.response?.data?.errorMessage || e.message);
    }
  }
);

export const createUser = createAsyncThunk(
  '@@user/create-user',
  async (data, {extra: {client, api}, rejectWithValue}) => {
    try {
      return await client.post(api.SIGNUP, data);
    } catch (e) {
      return rejectWithValue(
        e?.response?.data?.fieldError?.exceptionMessage || e.message
      );
    }
  }
);

const initialState = {
  status: 'idle',
  error: '',
  user: JSON.parse(localStorage.getItem('user')),
};

const userSlice = createSlice({
  name: '@@user',
  initialState,
  reducers: {
    logout: state => {
      localStorage.clear();
      state.status = 'idle';
      state.error = '';
      state.user = null;
    },
    cleanUserStatus: state => {
      state.status = 'idle';
      state.error = '';
    },
    expireToken: state => {
      state.status = 'rejected';
      state.error = 'Token has been expired, re-login please.'
      state.user = null;
      localStorage.clear();
    }
  },
  extraReducers: builder => {
    builder
      .addCase(loadUser.fulfilled, (state, action) => {
        state.status = 'received';
        const {token, ...user} = action.payload.data;
        const role = user.role
          .substring(user.role.indexOf('_') + 1)
          .toLowerCase();
        user.expireTime = JSON.parse(atob(token.split('.')[1])).iat + TOKEN_TERM;

        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify({...user, role}));
        state.user = {...user, role};
      })
      .addCase(createUser.fulfilled, state => {
        state.status = 'created';
      })
      .addMatcher(
        action => action.type.match(/^@@user\/.*\/pending$/),
        state => {
          state.status = 'loading';
          state.error = '';
        }
      )
      .addMatcher(
        action => action.type.match(/^@@user\/.*\/rejected$/),
        (state, action) => {
          state.status = 'rejected';
          state.error = action.payload;
        }
      );
  },
});

export const {logout, cleanUserStatus, expireToken} = userSlice.actions;
export const userReducer = userSlice.reducer;
