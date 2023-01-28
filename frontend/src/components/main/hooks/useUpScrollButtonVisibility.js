import {useEffect, useRef, useState} from 'react';

export const useUpScrollButtonVisibility = upScrollElement => {
  const [upScrollButtonVisibility, setUpScrollButtonVisibility] =
    useState(false);
  const observer = useRef();

  useEffect(() => {
    if (observer.current) {
      observer.current.disconnect();
    }

    const options = {
      rootMargin: '300px 0px 0px 0px',
    };

    const callback = entries => {
      setUpScrollButtonVisibility(!entries[0].isIntersecting);
    };

    observer.current = new IntersectionObserver(callback, options);
    observer.current.observe(upScrollElement.current);
  }, [upScrollElement]);

  return upScrollButtonVisibility;
};
