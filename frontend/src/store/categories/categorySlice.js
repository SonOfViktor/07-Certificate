import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';

export const loadCategories = createAsyncThunk(
  '@@categories/load-categories',
  async (_, {extra: {client, api}}) => {
    return client.get(api.CATEGORIES);
  }
);

const initialState = [];

const categoriesSlice = createSlice({
  name: '@@categories',
  initialState,
  extraReducers: builder => {
    builder.addCase(loadCategories.fulfilled, (_, action) => {
      return action.payload.data;
    });
  },
});

export const categoriesReducer = categoriesSlice.reducer;
