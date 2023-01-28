import {useCallback} from 'react';
import {useSearchParams} from 'react-router-dom';

let timerId;

export const useQueryParams = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const initStateValue = (name, defaultValue) => {
    return searchParams.get(name) || defaultValue;
  };

  const changeQueryParams = useCallback(
    (param, value, millis = 0) => {
      clearTimeout(timerId);

      timerId = setTimeout(() => {
        setSearchParams(prev => {
          if (value) {
            prev.set(param, value);
          } else {
            prev.delete(param);
          }

          return prev;
        });
      }, millis);
    },
    [setSearchParams]
  );

  return [changeQueryParams, initStateValue];
};
