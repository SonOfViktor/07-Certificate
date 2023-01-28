import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {useUpScrollButtonVisibility} from '../../../components/main/hooks/useUpScrollButtonVisibility';
import UpScrollButton from '../../../components/main/UpScrollButton';

jest.mock('../../../components/main/hooks/useUpScrollButtonVisibility');

describe('coupon list component', () => {
  beforeEach(() => {
    window.IntersectionObserver = jest.fn().mockReturnValue({
      observe: () => null,
      unobserve: () => null,
      disconnect: () => null,
    });
  });

  test('up scroll button default visibility', () => {
    render(<UpScrollButton upScrollElement={{}} />);

    expect(screen.queryByTestId('arrow-upward-icon')).toBeNull();
  });

  test('up scroll button visible', () => {
    useUpScrollButtonVisibility.mockReturnValue(true);

    render(<UpScrollButton upScrollElement={{}} />);

    expect(screen.getByTestId('ArrowUpwardIcon')).toBeInTheDocument();
  });

  test('click up scroll button', () => {
    useUpScrollButtonVisibility.mockReturnValue(true);
    window.scrollTo = jest.fn();

    render(<UpScrollButton upScrollElement={{}} />);

    expect(window.scrollTo).not.toHaveBeenCalled();
    userEvent.click(screen.getByRole('button'));
    expect(window.scrollTo).toHaveBeenCalledWith({top: 0, behavior: 'smooth'});
  });
});
