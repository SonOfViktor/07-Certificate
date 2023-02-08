import {act, render, screen} from '@testing-library/react';
import SuccessSnackbar from '../../../components/ui/SuccessSnackbar';

describe('success snackbar test', () => {
  beforeAll(() => {
    jest.useFakeTimers();
  });

  test('open-close success snackbar', () => {
    render(<SuccessSnackbar message="Open" isShow={true} />);

    expect(screen.getByText('Open')).toBeInTheDocument();

    act(() => {
      jest.runAllTimers();
    });

    expect(screen.queryByText('Open')).toBeNull();
  });

  afterAll(() => {
    jest.useRealTimers();
  });
});
