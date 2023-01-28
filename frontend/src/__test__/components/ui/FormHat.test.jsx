import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import FormHat from '../../../components/ui/FormHat';

describe('form hat component', () => {
  test('form title', () => {
    render(<FormHat title="Title" />);

    expect(screen.getByText('Title')).toBeInTheDocument();
  });

  test('click on close button', () => {
    const onClose = jest.fn();

    render(<FormHat onClose={onClose} />);

    userEvent.click(screen.getByTestId('CloseIcon'));

    expect(onClose).toHaveBeenCalled();
  });
});
