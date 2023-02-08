import {createSlice} from '@reduxjs/toolkit';

const initialState = {
  sort: {field: 'createDate', order: 'ASC'},
  search: '',
  description: '',
  tags: [],
  category: '',
};

const filterSlice = createSlice({
  name: '@@filter',
  initialState,
  reducers: {
    setFilter: (_, action) => {
      const newState = {...initialState};
      Object.keys(action.payload)
        .filter(key => newState.hasOwnProperty(key))
        .forEach(key => {
          if (key === 'sort') {
            const [field, order] = action.payload[key].split(',');
            newState[key] = {field, order};
          } else if (key === 'tags') {
            const queryTags = action.payload[key].split(',');
            newState[key] = queryTags.filter(
              (tag, index) => queryTags.indexOf(tag) === index
            );
          } else {
            newState[key] = action.payload[key];
          }
        });
      return newState;
    },
    clearFilter: () => initialState,
  },
});

export const {setFilter, clearFilter} = filterSlice.actions;
export const filterReducer = filterSlice.reducer;
