import {selectCategories} from '../../../store/categories/categorySelector';
import {categoryData} from './testData';

const state = {categories: categoryData};

describe('category selectors', () => {
  test('select all names of categories', () => {
    expect(selectCategories(state)).toEqual(['Auto', 'Tech', 'Food']);
  });
});
