import {render, screen} from '@testing-library/react';
import {renderWithMui} from '../../helpers/renderWithMui';
import * as reduxHooks from 'react-redux';
import * as favoriteActions from '../../../store/favorite/favoriteSlice';
import * as bucketActions from '../../../store/bucket/bucketSlice';
import {createCertificateList} from '../../helpers/certificateHelper';
import CouponCardContent from '../../../components/main/CouponCardContent';
import userEvent from '@testing-library/user-event';

jest.mock('react-redux');

describe('coupon card content', () => {
  const certificate = createCertificateList(1)[0];

  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');

  test('coupon card content render', () => {
    render(<CouponCardContent certificate={certificate} />);

    expect(screen.getByText('Certificate1')).toBeInTheDocument();
    expect(screen.getByText('#tag1, #tag2, #tag3')).toBeInTheDocument();
    expect(screen.getByText('Duration 30 days')).toBeInTheDocument();
    expect(screen.getByText('$50')).toBeInTheDocument();
  });

  test('favorite icon color when certificate is favorite', () => {
    mockedUseSelector.mockReturnValue(true);

    renderWithMui(<CouponCardContent certificate={certificate} />);

    const element = screen.getByTestId('FavoriteIcon');
    const styles = getComputedStyle(element);

    expect(styles.color).toBe('rgb(248, 114, 197)');
  });

  test('favorite icon color when certificate is not favorite', () => {
    mockedUseSelector.mockReturnValue(false);

    renderWithMui(<CouponCardContent certificate={certificate} />);

    const element = screen.getByTestId('FavoriteIcon');
    const styles = getComputedStyle(element);

    expect(styles.color).toBe('rgba(0, 0, 0, 0.54)');
  });

  describe('user events on coupon card content', () => {
    const dispatch = jest.fn();
    const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');

    beforeEach(() => {
      mockedUseDispatch.mockReturnValue(dispatch);
    });

    test('toggle favorite icon when certificate is favorite', () => {
      mockedUseSelector.mockReturnValue(true);

      const mockedAction = jest.spyOn(
        favoriteActions,
        'deleteFavoriteCertificate'
      );

      render(<CouponCardContent certificate={certificate} />);

      const favorite = screen.getByTestId('FavoriteIcon');

      expect(dispatch).not.toHaveBeenCalled();
      userEvent.click(favorite);
      expect(dispatch).toHaveBeenCalled();
      expect(mockedAction).toHaveBeenCalledWith(1);
    });

    test('toggle favorite icon when certificate is not favorite', () => {
      mockedUseSelector.mockReturnValue(false);

      const mockedAction = jest.spyOn(
        favoriteActions,
        'addFavoriteCertificate'
      );

      render(<CouponCardContent certificate={certificate} />);

      const favorite = screen.getByTestId('FavoriteIcon');

      expect(dispatch).not.toHaveBeenCalled();
      userEvent.click(favorite);
      expect(dispatch).toHaveBeenCalled();
      expect(mockedAction).toHaveBeenCalledWith(certificate);
    });

    test('click add certificate to cart', () => {
      const mockedAction = jest.spyOn(bucketActions, 'addBucketCertificate');

      render(<CouponCardContent certificate={certificate} />);

      const addButton = screen.getByText('Add to Cart');

      expect(dispatch).not.toHaveBeenCalled();
      userEvent.click(addButton);
      expect(dispatch).toHaveBeenCalled();
      expect(mockedAction).toHaveBeenCalledWith(certificate);
    });
  });
});
