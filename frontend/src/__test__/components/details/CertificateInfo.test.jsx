import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as favoriteActions from '../../../store/favorite/favoriteSlice';
import * as bucketActions from '../../../store/bucket/bucketSlice';
import CertificateInfo from '../../../components/details/CertificateInfo';
import {createCertificate} from '../../helpers/certificateHelper';

jest.mock('react-redux');

describe('coupon list component', () => {
  const certificate = createCertificate(
    1,
    'Certificate',
    'some description',
    20,
    50,
    [{name: 'tag1'}, {name: 'tag2'}, {name: 'tag3'}]
  );
  certificate.category = 'Tech';

  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');

  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
  });

  test('certificate info component render', () => {
    render(<CertificateInfo {...certificate} />);

    expect(screen.getByText('Certificate')).toBeInTheDocument();
    expect(screen.getByText('Tech')).toBeInTheDocument();
    expect(screen.getByText('tag2')).toBeInTheDocument();
    expect(screen.getByText('20 days')).toBeInTheDocument();
    expect(screen.getByText('$50')).toBeInTheDocument();
  });

  test('toggle favorite icon when certificate is favorite', () => {
    mockedUseSelector.mockReturnValue(true);

    const mockedAction = jest.spyOn(
      favoriteActions,
      'deleteFavoriteCertificate'
    );

    render(<CertificateInfo {...certificate} />);

    const favorite = screen.getByTestId('FavoriteIcon');

    expect(dispatch).not.toHaveBeenCalled();
    userEvent.click(favorite);
    expect(dispatch).toHaveBeenCalled();
    expect(mockedAction).toHaveBeenCalledWith(1);
  });

  test('toggle favorite icon when certificate is not favorite', () => {
    mockedUseSelector.mockReturnValue(false);

    const mockedAction = jest.spyOn(favoriteActions, 'addFavoriteCertificate');

    render(<CertificateInfo {...certificate} />);

    const favorite = screen.getByTestId('FavoriteIcon');

    expect(dispatch).not.toHaveBeenCalled();
    userEvent.click(favorite);
    expect(dispatch).toHaveBeenCalled();
    expect(mockedAction).toHaveBeenCalledWith(certificate);
  });

  test('click add certificate to cart', () => {
    const mockedAction = jest.spyOn(bucketActions, 'addBucketCertificate');

    render(<CertificateInfo {...certificate} />);

    const addButton = screen.getByTestId('ShoppingCartIcon');

    expect(dispatch).not.toHaveBeenCalled();
    userEvent.click(addButton);
    expect(dispatch).toHaveBeenCalled();
    expect(mockedAction).toHaveBeenCalledWith(certificate);
  });
});
