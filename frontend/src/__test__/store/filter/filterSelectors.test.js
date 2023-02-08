import {filterState} from './testData';
import {
  selectCategory,
  selectFilter,
  selectSort,
  selectTags,
} from '../../../store/filter/filterSelectors';

const testedState = {
  filter: filterState,
};

describe('filter selectors', () => {
  test('select filter', () => {
    expect(selectFilter(testedState)).toEqual(testedState.filter);
  });

  test('select category from the filter', () => {
    expect(selectCategory(testedState)).toBe('Sport');
  });

  test('select sort from the filter', () => {
    expect(selectSort(testedState)).toEqual({field: 'name', order: 'DESC'});
  });

  test('select tags from the filter', () => {
    expect(selectTags(testedState)).toEqual(['tag1', 'tag2']);
  });
});
