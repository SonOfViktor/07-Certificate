import {
  clearFilter,
  filterReducer,
  setFilter,
} from '../../../store/filter/filterSlice';
import {filterState} from './testData';

describe('filter slice reducer', () => {
  const initialState = {
    sort: {field: 'createDate', order: 'ASC'},
    search: '',
    description: '',
    tags: [],
    category: '',
  };

  test('set filter action', () => {
    const payload = {
      ...filterState,
      sort: 'name,DESC',
      tags: 'tag1,tag2,tag2',
      extra: 'extra data',
    };

    const actual = filterReducer(initialState, setFilter(payload));

    expect(actual).toEqual(filterState);
  });

  test('set filter with unfit search parameters', () => {
    const payload = {
      param1: 'param1',
      param2: 'param2',
    };

    const actual = filterReducer(initialState, setFilter(payload));

    expect(actual).toEqual(initialState);
  });

  test('clear filter', () => {
    const actual = filterReducer(filterState, clearFilter());

    expect(actual).toEqual(initialState);
  });
});
