import axios from 'axios';
import {
  categoriesReducer,
  loadCategories,
} from '../../../store/categories/categorySlice';
import {categoryData} from './testData';

jest.mock('axios');

describe('categories slice', () => {
  test('load categories thunk', async () => {
    axios.get.mockReturnValue(categoryData);
    const dispatch = jest.fn();

    const thunk = loadCategories();
    await thunk(dispatch, null, {client: axios, api: ''});

    const actual = dispatch.mock.calls[1][0].payload;
    expect(actual).toEqual(categoryData);
  });

  test('fulfilled load categories action', () => {
    const actual = categoriesReducer(
      [],
      loadCategories.fulfilled({data: categoryData})
    );

    expect(actual).toEqual(categoryData);
  });
});
