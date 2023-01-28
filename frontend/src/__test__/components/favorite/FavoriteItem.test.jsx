import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {useDispatch} from 'react-redux';
import FavoriteItem from '../../../components/favorite/FavoriteItem';
import * as bucketActions from '../../../store/bucket/bucketSlice';
import * as favoriteActions from '../../../store/favorite/favoriteSlice';
import {createCertificate} from '../../helpers/certificateHelper';

jest.mock('react-redux', () => ({
  useDispatch: jest.fn(),
}));

describe('favorite item component', () => {
  const dispatch = jest.fn();
  const certificate = createCertificate(
    3,
    'Certificate',
    'description',
    0,
    50,
    []
  );

  beforeEach(() => {
    useDispatch.mockReturnValue(dispatch);
  });

  test('favorite item render', () => {
    render(<FavoriteItem certificate={certificate} />);

    expect(screen.getByAltText(/certificate/i)).toHaveAttribute(
      'src',
      '/api/v1/certificates/3/image'
    );
    expect(screen.getByText('$50')).toBeInTheDocument();
  });

  test('click on add to shopping cart', () => {
    const addBucketAction = jest.spyOn(bucketActions, 'addBucketCertificate');

    render(<FavoriteItem certificate={certificate} />);

    const shoppingCartButton = screen.getByTestId('ShoppingCartIcon');

    expect(dispatch).not.toHaveBeenCalled();
    userEvent.click(shoppingCartButton);
    expect(dispatch).toHaveBeenCalled();
    expect(addBucketAction).toHaveBeenCalledWith(certificate);
  });

  test('click on delete favorite certificate', () => {
    const deleteFavoriteAction = jest.spyOn(
      favoriteActions,
      'deleteFavoriteCertificate'
    );

    render(<FavoriteItem certificate={certificate} />);

    const deleteFavoriteButton = screen.getByTestId('DeleteIcon');

    expect(dispatch).not.toHaveBeenCalled();
    userEvent.click(deleteFavoriteButton);
    expect(dispatch).toHaveBeenCalled();
    expect(deleteFavoriteAction).toHaveBeenCalledWith(3);
  });
});
