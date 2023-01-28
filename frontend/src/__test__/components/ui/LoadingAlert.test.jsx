import {render, screen} from '@testing-library/react';
import LoadingAlert from '../../../components/ui/LoadingAlert';
import * as reduxHooks from 'react-redux';

jest.mock("react-redux");

describe('loading alert', () => {
  const mockUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');

  beforeEach(() => {
    mockUseDispatch.mockReturnValue(jest.fn());
  })

  test('loading alert render no error', () => {
    const {container} = render(<LoadingAlert />);

    expect(container).toBeEmptyDOMElement();
  });

  test('loading alert render with error', () => {
    render(<LoadingAlert status="rejected" error="Error message" />);

    expect(screen.getByText('Error message')).toBeInTheDocument();
  });

  test('clean error message', () => {
    const cleanErrorAction = jest.fn();

    const {unmount} = render(<LoadingAlert status="rejected" cleanErrorAction={cleanErrorAction}/>);

    expect(cleanErrorAction).not.toHaveBeenCalled();
    unmount();
    expect(cleanErrorAction).toHaveBeenCalled();
  })

  test('status is not rejected', () => {
    const cleanErrorAction = jest.fn();

    const {unmount} = render(<LoadingAlert status="idle" cleanErrorAction={cleanErrorAction}/>);

    unmount();
    expect(cleanErrorAction).not.toHaveBeenCalled();
  })
});
