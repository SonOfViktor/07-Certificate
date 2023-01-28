import {screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ConfirmResetButton from '../../../components/ui/ConfirmResetButton';
import {renderWithMui} from '../../helpers/renderWithMui';

describe('confirm reset button', () => {
  test('first style button render', () => {
    renderWithMui(<ConfirmResetButton>Button</ConfirmResetButton>);

    const button = screen.getByRole('button');
    const styles = getComputedStyle(button);

    expect(styles.color).toBe('white');
    expect(styles.backgroundColor).toBe('rgb(64, 212, 126)');
  });

  test('second style button render', () => {
    renderWithMui(
      <ConfirmResetButton cancel="true">Button</ConfirmResetButton>
    );

    const button = screen.getByRole('button');
    const styles = getComputedStyle(button);

    expect(styles.color).toBe('rgb(117, 117, 117)');
    expect(styles.backgroundColor).toBe('white');
  });

  test('click button', () => {
    const onClick = jest.fn();

    renderWithMui(
      <ConfirmResetButton onClick={onClick}>Button</ConfirmResetButton>
    );

    const button = screen.getByRole('button');
    userEvent.click(button);

    expect(onClick).toHaveBeenCalled();
  });
});
