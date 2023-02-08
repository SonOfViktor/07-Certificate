import {
  selectFavorite,
  selectFavoriteLength,
  selectIsFavorite,
} from '../../../store/favorite/favoriteSelectors';
import {favoriteCertificates} from './testData';

describe('favorite certificates selector', () => {
  const state = {favorite: favoriteCertificates};

  test('select all favorite certificates', () => {
    expect(selectFavorite(state)).toEqual(state.favorite);
  });

  test('select favorite certificates quantity', () => {
    expect(selectFavoriteLength(state)).toBe(3);
  });

  test('select certificate presence in the favorite list', () => {
    expect(selectIsFavorite(state, 3)).toBeTruthy();
  });

  test('select certificate absence in the favorite list', () => {
    expect(selectIsFavorite(state, 4)).toBeFalsy();
  });
});
