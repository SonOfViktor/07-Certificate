import {
  addFavoriteCertificate,
  deleteFavoriteCertificate,
  favoriteReducer,
} from '../../../store/favorite/favoriteSlice';
import {favoriteCertificates} from './testData';

describe('favorite certificates reducer', () => {
  const certificate = {id: 2, name: 'Certificate2'};

  test('add certificate to the favorite list', () => {
    const actual = favoriteReducer(
      favoriteCertificates,
      addFavoriteCertificate(certificate)
    );
    const expected = [...favoriteCertificates, certificate];

    expect(actual).toEqual(expected);
  });

  test('add certificate to the empty favorite list', () => {
    const actual = favoriteReducer([], addFavoriteCertificate(certificate));
    const expected = [certificate];

    expect(actual).toEqual(expected);
  });

  test('delete certificate from the favorite list', () => {
    const actual = favoriteReducer(
      favoriteCertificates,
      deleteFavoriteCertificate(3)
    );
    const expected = [
      {giftCertificateId: 1, name: 'Certificate1'},
      {giftCertificateId: 8, name: 'Certificate8'},
    ];

    expect(actual).toEqual(expected);
  });
});
